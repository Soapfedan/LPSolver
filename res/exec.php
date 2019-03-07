<?php
$output = array();
exec("java -jar Solver.jar 20 6 0 pref.txt", $output); 
echo "<pre>";
print_r($output);
?>
