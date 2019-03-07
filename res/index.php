<?php

/**
 * out: $preferences
 * [
 *  ...
 *  10 => [ 1 => 50, 2 => -1, ... ]
 *  11 => [ 1 => 2, ... ],
 *  indice_azienda => [ indice_azienda => valore_preferenza ],
 *  ...
 * ]
 */
function createRandomPreferences (
    $companies_num,
    $explicit_preferences_mean_num = 1,
    $explicit_preference_value = 50,
    $min_preference_value = 1,
    $max_preference_value = 1
) {

    if ($explicit_preferences_mean_num > $companies_num && $explicit_preferences_mean_num < 0) {
        throw new Exception("il numero medio di preferenze esplicite deve essere inferiore al numero di aziende");
    }

    if ($explicit_preference_value < $max_preference_value ) {
        throw new Exception("il valore esplicito di preferenza deve essere maggiore di ogni possibile valore di preferenza non esplicito");
    }

    if ($max_preference_value < $min_preference_value ) {
        throw new Exception("deve valere sempre: max_preference_value >= min_preference_value");
    }

    $explicit_preferences_percentage = $explicit_preferences_mean_num / $companies_num;
    $ret = [];

    for ($i=1; $i <= $companies_num; $i++)
    {
        $sub_ret = [];

        for ($j = 1; $j <= $companies_num && $i > $j; $j++)
        {
            if (mt_rand(0,100) < floor($explicit_preferences_percentage * 100) ) {
                $sub_ret[$j] = $explicit_preference_value;
            } else {
                $sub_ret[$j] = mt_rand($min_preference_value,$max_preference_value);
            }
        }

        $ret[$i] = $sub_ret;
    }

    return $ret;

}


function createPreferencesFile ($preferences, $file_path) {
    $file = fopen($file_path, "w") or die("Unable to open file!");
    foreach ($preferences as $k1 => $v1) {
        foreach ($v1 as $k2 => $v2) {
            fwrite($file, "$k1,$k2,$v2\n");
        }
    }
    fclose($file);
}
