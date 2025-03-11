Feature: Car valuation on confused.com

  # The test will always fail as there are mismatches in the expected data in the output file and actual data on the website for all of the registration numbers in the input file
  # Additional registration numbers are added to the input file to check : 1. UK registration pattern check 2. registration number present in input file but not in output file
  # Please see the log file in the logs folder to see the errors and mismatches reported for each of the registration number
  Scenario: Verify car details from the car valuation website
    Given the user reads the car registration numbers from the given input file car_input - V6.txt
    When the user searches for each car on the car valuation website confused.com to compare with the expected details in the output file car_output - V6.txt
    Then the user can see comparison results for each of the car