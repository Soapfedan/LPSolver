<?php
$output = array();
ini_set('max_execution_time', 300);
exec("java -jar Solver.jar 10 4 2 results/10_4_2.txt", $output); 
echo "<pre>";
print_r($output);
?>
