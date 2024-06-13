def sort_lines_and_add_semicolon(input_file, output_file):
    with open(input_file, 'r') as file:
        lines = file.readlines()

    # Strip any existing whitespace/newlines and sort lines
    lines = [line.strip() for line in lines]
    lines.sort()

    # Add a semicolon to the end of each line
    lines = [line + ';' for line in lines]

    # Write the sorted lines to the output file
    with open(output_file, 'w') as file:
        for line in lines:
            file.write(line + '\n')

# Example usage
input_file = 'output.txt'
output_file = 'sorted_output.txt'
sort_lines_and_add_semicolon(input_file, output_file)

