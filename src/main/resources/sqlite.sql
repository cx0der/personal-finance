INSERT INTO account (id, name, description, currency, type) VALUES
(1, 'Equity:Opening Balance', 'Opening balances', 'GBP', 'Equity'),
(2, 'Assets:Barclays', 'Checking Account', 'GBP', 'Asset'),
(3, 'Assets:Monzo', 'Checking Account', 'GBP', 'Asset'),
(4, 'Assets:Cash In Hand', 'Checking Account', 'GBP', 'Asset'),
(5, 'Income:Salary', 'Salary Account', 'GBP', 'Income'),
(6, 'Income:Interest:Bank Account Interest', 'Current and savings interest', 'GBP', 'Income'),
(7, 'Income:Interest:Other Interest', 'Other interests', 'GBP', 'Income'),
(8, 'Income:Bonus', 'Bonus', 'GBP', 'Income'),
(9, 'Liabilities:Car Loan', 'Car loan', 'GBP', 'Liability'),
(10, 'Liabilities:Credit Card:Tymit', 'Tymit', 'GBP', 'Liability'),
(11, 'Expenses:Utilities:Electricity', 'Electricity', 'GBP', 'Expense'),
(12, 'Expenses:Utilities:Water', 'Water', 'GBP', 'Expense'),
(13, 'Expenses:Utilities:Phone', 'Mobile Phone', 'GBP', 'Expense'),
(14, 'Expenses:Utilities:Broadband', 'Home Broadband', 'GBP', 'Expense'),
(15, 'Expenses:Clothes', 'Clothes', 'GBP', 'Expense'),
(16, 'Expenses:Education', 'Education', 'GBP', 'Expense'),
(17, 'Expenses:Dining', 'Dining', 'GBP', 'Expense'),
(18, 'Expenses:Childcare', 'Childcare', 'GBP', 'Expense'),
(19, 'Expenses:Personal Care', 'Personal Care', 'GBP', 'Expense'),
(20, 'Expenses:Auto:Fuel', 'Fuel', 'GBP', 'Expense'),
(21, 'Expenses:Auto:Parking', 'Parking', 'GBP', 'Expense'),
(22, 'Expenses:Auto:Maintenance', 'Maintenance', 'GBP', 'Expense'),
(23, 'Expenses:Insurance:Car', 'Car Insurance', 'GBP', 'Expense'),
(24, 'Expenses:Groceries', 'Groceries', 'GBP', 'Expense'),
(25, 'Expenses:Rent', 'Rent', 'GBP', 'Expense'),
(26, 'Expenses:Subscriptions', 'Subscriptions', 'GBP', 'Expense'),
(27, 'Expenses:Shopping', 'Subscriptions', 'GBP', 'Expense'),
(28, 'Expenses:Online Shopping', 'Online shopping', 'GBP', 'Expense'),
(29, 'Expenses:Travel', 'Travel', 'GBP', 'Expense'),
(30, 'Expenses:Public Transportation', 'Public transport', 'GBP', 'Expense'),
(31, 'Expenses:Taxes:Income Tax', 'Income Tax', 'GBP', 'Expense'),
(32, 'Expenses:Taxes:Council Tax', 'Council Tax', 'GBP', 'Expense'),
(33, 'Expenses:Miscellaneous', 'Miscellaneous', 'GBP', 'Expense'),
(34, 'Expenses:Uncategorized', 'Uncategorized', 'GBP', 'Expense'),
(35, 'Liabilities:Loqbox', 'LoqBox', 'GBP', 'Liability'),
(36, 'Liabilities:Credit Card:Vanquis', 'Vanquis', 'GBP', 'Liability');

INSERT INTO auto_mapping (description, account_id) VALUES
('Expenses:Uncategorized', 34),
('poundland', 27),
('sainsbury', 24),
('aldi', 24),
('show market', 24),
('uber', 30),
('bolt.eu', 30),
('ikea', 27),
('asda', 24),
('mcdonanlds', 17),
('w m morrison plc', 24),
('tesco', 17),
('currys online', 28),
('amz', 28),
('argos', 28),
('google', 28),
('metrolink', 30),
('admiral', 23),
('shell', 20),
('3781278/94037555', 9),
('tfgm', 30),
('xexec', 33),
('boots', 19),
('lidl', 24),
('octopus', 11),
('HM Hennes Mauritz', 15),
('trainline', 30),
('nalan homes', 25),
('kpmg', 5),
('worldwide foods', 24),
('williams motor com', 9),
('loqbox', 35);