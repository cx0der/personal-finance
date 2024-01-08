package com.moveableapps.pf.data;

import com.moveableapps.pf.entities.Account;
import com.moveableapps.pf.entities.AccountType;
import com.moveableapps.pf.entities.AutoMapping;
import com.moveableapps.pf.entities.Split;
import com.moveableapps.pf.entities.Transaction;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Scanner;

public class DBRepository implements Repository {

    private static final DateFormat sqliteDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final String jdbcUrl;
    protected Connection connection;

    public DBRepository(String dbDirectory) {
        jdbcUrl = "jdbc:sqlite:" + dbDirectory + File.separator + "personal-finance.db";
        this.connection = initialize(dbDirectory);
    }

    @Override
    public long addAccount(Account account) {
        String sql = """
                INSERT INTO account(name, description, currency, type)
                VALUES(?, ?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, account.name());
            statement.setString(2, account.description());
            statement.setString(3, account.currency());
            statement.setString(4, account.type().value());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
                throw new RuntimeException("Account Name is not unique");
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Account> getAllAccounts() {
        String sql = """
                SELECT id, name, description, currency, type, created_at, updated_at
                FROM account
                """;
        List<Account> accounts = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                accounts.add(parseResultSetForAccount(rs));
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    public Optional<Account> getAccountByName(String accountName) {
        Optional<Account> mayBeAccount = Optional.empty();
        String sql = """
                SELECT id, name, description, currency, type, created_at, updated_at
                FROM account WHERE name LIKE ?
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, "%" + accountName + "%");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                mayBeAccount = Optional.of(parseResultSetForAccount(rs));
            }
        } catch (SQLException e) {
//            logger.error("Error fetching account: " + e.getLocalizedMessage());
        } catch (ParseException e) {
//            logger.error("Error parsing data: " + e.getLocalizedMessage());
        }
        return mayBeAccount;
    }

    public long addTransaction(Transaction transaction) {

        String sql = """
                INSERT INTO txn(post_date, entry_date, description)
                VALUES(?, ?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, sqliteDateFormat.format(transaction.postDate()));
            statement.setString(2, sqliteDateFormat.format(transaction.entryDate()));
            statement.setString(3, transaction.description());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        String sql = """
                SELECT id, post_date, entry_date, description FROM txn
                """;
        List<Transaction> transactions = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);

            while (rs.next()) {
                long id = rs.getLong("id");
                String postDateString = rs.getString("post_date");
                String entryDateString = rs.getString("entry_date");
                Date postDate = sqliteDateFormat.parse(postDateString);
                Date entryDate = sqliteDateFormat.parse(entryDateString);
                String description = rs.getString("description");
                Transaction transaction = new Transaction(postDate, entryDate, description, OptionalLong.of(id));
                transactions.add(transaction);
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }

    public int addSplits(long transactionId, List<Split> splits) {
        String sql = """
                INSERT INTO split(account_id, txn_id, is_reconciled, reconciled_at, value_num, value_denom)
                VALUES(?, ?, ?, ?, ?, ?)
                """;
        int insertCount = 0;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (Split split : splits) {
                statement.setLong(1, split.accountId());
                statement.setLong(2, transactionId);
                if (split.reconciledAt().isEmpty()) {
                    statement.setString(3, "n");
                    statement.setNull(4, Types.VARCHAR);
                } else {
                    statement.setString(3, "y");
                    statement.setString(4, sqliteDateFormat.format(split.reconciledAt().get()));
                }
                statement.setLong(5, split.valueNum());
                statement.setInt(6, split.valueDenom());
                statement.executeUpdate();
                insertCount++;
                statement.clearParameters();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return insertCount;
    }

    @Override
    public List<Split> getSplitsForTransaction(long transactionId) {
        String sql = """
                SELECT id, account_id, txn_id, is_reconciled, reconciled_at, value_num, value_denom
                FROM split
                WHERE txn_id = ?
                """;
        List<Split> splits = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, transactionId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                long id = rs.getLong("id");
                long accountId = rs.getLong("account_id");
                long txnId = rs.getLong("txn_id");
                String isReconciledString = rs.getString("is_reconciled");
                char isReconciled = isReconciledString.charAt(0);
                String reconciledAtString = rs.getString("reconciled_at");
                Date reconciledAt = null;
                if (reconciledAtString != null) {
                    reconciledAt = sqliteDateFormat.parse(reconciledAtString);
                }
                long valueNum = rs.getLong("value_num");
                int valueDenom = rs.getInt("value_denom");
                Split split = new Split(accountId, isReconciled, valueNum, valueDenom,
                        Optional.ofNullable(reconciledAt), OptionalLong.of(txnId), OptionalLong.of(id));
                splits.add(split);
            }
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
        return splits;
    }

    @Override
    public long addAutoMapping(AutoMapping mapping) {
        String sql = """
                INSERT INTO auto_mapping(description, account_id)
                VALUES(?, ?)
                """;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, mapping.description());
            statement.setLong(2, mapping.accountId());
            statement.executeUpdate();

            ResultSet rs = statement.getGeneratedKeys();
            return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, AutoMapping> getAllAutoMappings() {
        Map<String, AutoMapping> mappings = new HashMap<>();
        String sql = """
                SELECT id, description, account_id FROM auto_mapping
                """;
        try (Statement statement = connection.createStatement()) {
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                long id = rs.getLong("id");
                String description = rs.getString("description");
                long accountId = rs.getLong("account_id");
                AutoMapping mapping = new AutoMapping(description, accountId, OptionalLong.of(id));
                mappings.put(description, mapping);
            }
        } catch (SQLException e) {
//            logger.error("Error fetching mappings: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
        return mappings;
    }

    private Connection initialize(String dbDirectory) {
        Path directory = Path.of(dbDirectory);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
//                logger.error("Unable to create database directory: " + e.getLocalizedMessage());
                throw new RuntimeException(e);
            }
        }
        try {
            Connection connection = DriverManager.getConnection(jdbcUrl);
            createDatabase(connection, DBRepository.class.getClassLoader().getResourceAsStream("sqlite_bootstrap.sql"));
            return connection;
        } catch (SQLException e) {
//            logger.error("Unable to create SQLite DB: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private void createDatabase(Connection connection, InputStream is) {
        if (is == null) {
//            logger.error("Unable to read SQL script");
            throw new RuntimeException("Unable to read SQL script");
        }
        Scanner scanner = new Scanner(is);
        scanner.useDelimiter("(;(\\r)?\\n)|(--\\n)");

        try (Statement statement = connection.createStatement()) {
            while (scanner.hasNext()) {
                String sqlStatementString = scanner.next();
                if (sqlStatementString.startsWith("/*!") && sqlStatementString.endsWith("*/")) {
                    int i = sqlStatementString.indexOf(' ');
                    sqlStatementString = sqlStatementString.substring(i + 1, sqlStatementString.length() - "*/".length());
                }
                if (!sqlStatementString.trim().isEmpty()) {
                    statement.execute(sqlStatementString);
                }
            }
        } catch (SQLException e) {
//            logger.error("Error while creating database: " + e.getLocalizedMessage());
            throw new RuntimeException(e);
        }
    }

    private Account parseResultSetForAccount(ResultSet rs) throws SQLException, ParseException {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String currency = rs.getString("currency");
        String type = rs.getString("type");
        String createdAtString = rs.getString("created_at");
        String updatedAtString = rs.getString("updated_at");
        Date createdAt = sqliteDateFormat.parse(createdAtString);
        Date updatedAt = sqliteDateFormat.parse(updatedAtString);
        return new Account(OptionalLong.of(id), name, description, currency, AccountType.valueOf(type.toUpperCase()), createdAt, updatedAt);
    }
}
