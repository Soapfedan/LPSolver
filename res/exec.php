<?php
$output = array();
ini_set('max_execution_time', 300);
exec("java -jar Solver.jar 200 8 2 results/200_8_2.txt", $output); 
echo "<pre>";
print_r($output);
?>
