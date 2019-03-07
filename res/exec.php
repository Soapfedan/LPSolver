<?php
$output = array();
ini_set('max_execution_time', 300);
exec("java -jar Solver.jar 26 4 2 results/26_4_2.txt", $output); 
echo "<pre>";
print_r($output);
?>
