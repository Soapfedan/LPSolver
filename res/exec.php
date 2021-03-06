<?php

require_once 'index.php';

$companies_num = 10;
$rounds_num = 3;
$min_incontri = 0;
$explicit_preferences_mean_num = 8;
$explicit_preference_value = 50;
$min_preference_value = -10;
$max_preference_value = 10;
$to_print_stats = 1;

createPreferencesFile(
    createRandomPreferences($companies_num,$explicit_preferences_mean_num,$explicit_preference_value,$min_preference_value,$max_preference_value),
    "${companies_num}_${explicit_preferences_mean_num}_${explicit_preference_value}_${min_preference_value}_${max_preference_value}.txt"
);

$output = array();
ini_set('max_execution_time', 300);
exec("java -jar Solver.jar ${companies_num} ${rounds_num} ${min_incontri} ${companies_num}_${explicit_preferences_mean_num}_${explicit_preference_value}_${min_preference_value}_${max_preference_value}.txt ${to_print_stats}", $output); 
echo "<pre>";
print_r($output);
?>
