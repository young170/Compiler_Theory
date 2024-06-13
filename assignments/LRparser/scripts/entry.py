import csv

# Function to process the CSV file
def process_csv(input_file, output_file):
    with open(input_file, mode='r', newline='') as csvfile:
        csvreader = csv.reader(csvfile)
        
        # Skip the first 2 rows
        next(csvreader)
        next(csvreader)
        
        # Read the 3rd row and store in a str list called "symbols"
        symbols = next(csvreader)[1:]
        
        # Open the output file for writing
        with open(output_file, mode='w', newline='') as outfile:
            for row_idx, row in enumerate(csvreader):  # Start enumeration from 4 since we skipped 3 rows
                for col_idx, entry in enumerate(row[1:]):
                    if entry:  # Check if the entry is not empty
                        if entry[0] in ('s', 'r'):
                            print('action', 'row_idx:', row_idx, 'symbols[col_idx]:', symbols[col_idx], 'entry:', entry)
                            outfile.write(f'addAction({row_idx}, "{symbols[col_idx]}", "{entry}")\n')
                        elif entry[0].isdigit():
                            print('goto', 'row_idx:', row_idx, 'symbols[col_idx]:', symbols[col_idx], 'entry:', entry)
                            outfile.write(f'addGoto({row_idx}, "{symbols[col_idx]}", "{entry}")\n')

# Example usage
input_file = 'lr1_parsing_table.csv'
output_file = 'output.txt'
process_csv(input_file, output_file)

