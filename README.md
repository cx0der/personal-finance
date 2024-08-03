# Personal Finance
A simple [double entry bookkeeping](https://en.wikipedia.org/wiki/Double-entry_bookkeeping) command line application.

## Building from source

To build from source make sure you have JDK 17 or higher available.
```shell
./gradlew distTar
```

Running tests
```shell
./gradlew test
```
## Running

Once you have built the distribution, you can "install" the application using `./gradlew install`,
which will set the application at `build/install/pf/bin`. From here you can run using `./pf`.

### Usage

```shell
# Create account
$ ./pf add-account Expense:Groceries -c USD -t EXPENSE -d 'all groceries go here'

# List accounts
$ ./pf list-accounts

# Mapping allows loader to categories the transactions automatically
# This example allows you to categorise all transactions described as 'food mart'
# to Expense:Groceries 
$ ./pf add-mapping -d 'food mart' -t Expense:Groceries

# List all the mappings
$ ./pf list-mappings

# importing/loading transactions from a csv file
$ ./pf load -a Income:Salary --date-field 0 --desc-field 1 --amount-field 2 -f input.csv
```
