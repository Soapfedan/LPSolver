<?php
$output = array();
ini_set('max_execution_time', 300);
exec("java -jar Solver.jar 26 5 0 results/26_5_0.txt", $output); 
echo "<pre>";
print_r($output);
?>
