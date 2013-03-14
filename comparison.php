<?php

ini_set('display_errors',1);
error_reporting(E_ALL);

$data_colruyt = array(
	1.89,
	1.50,
	1.25,
        0.29,
        0.29,
        1.50,
        0.65
);

$data_aldi = json_decode('{
	"fore 12x50gr"			: {"id": 0, "price": 1.99},
	"vers sap bloedsinaas"		: {"id": 1, "price": 1.69},
	"schoenpoetsdoekjes 08B"	: {"id": 2, "price": 1.29},
        "bruin stokbroodje"             : {"id": 3, "price": 0.35},
        "wit stokbroodje"               : {"id": 4, "price": 0.35},
        "crunchy ontbijtgranen"         : {"id": 5, "price": 1.69},
        "cheddar smeltkaas 250gr"       : {"id": 6, "price": 0.79}
}', true);

//print_r($data_aldi);

/*if( !empty($_SERVER['HTTP_X_REQUESTED_WITH'] ) && strtolower( $_SERVER['HTTP_X_REQUESTED_WITH'] ) == 'xmlhttprequest')
{
	$data = $_POST["data"];
	$data = str_replace(",", ".", $data); // Replace decimal sign
	$data = explode("\n", $data); // Explode to array by newline

}*/

$data_ocr = "BTW BE 0465-569-910
        FIRE 1ZX50GR 1,99
        VERS SAP BLUEDSINAAS 1,69
        SCHUENPDETSDDEKJES DEB 1,29
        BRUIN STOKBRUUDJE 0,35
        WIT STUKBRUUDJE 0,35
        CRUNCHY UNTBIJTGRANEN 1,69
        CHEDDAR SMELTKAAS Z503 0,79
        BETALEN EURO 5,15
        éZ§:Es3§&£E¥' 7'
        MISTER CASH
        TOTAAL EURO
        TOTAL : 10 , 35
        XXXXXXXXXXXXXXXXXX16
        TERMINAL ID: 1716327
        DEBETKAART 10,35
        TERUG 2.20
        AANTAL ARTIKELS 7";

//$data = $_POST["data"];
$data = $data_ocr;
$data = str_replace(",", ".", $data); // Replace decimal sign
$data = explode("\n", $data); // Explode to array by newline

$ocr = array();

foreach ($data as $key => $value)
{
        //echo $value."<br>";
        if (preg_match('/[0-9]+\.[0-9]{2}/', $value, $matches)) // Match decimal number
        {
                # Successful match
                //echo "succes <br>";
                $lev = levenshtein("BETALEN EURO", trim(str_replace($matches[0], "", $value)));
                if ($lev < 5)
                {
                        break;
                }
                $ocr[] = array("product" => trim(str_replace($matches[0], "", $value)), "price" => $matches[0]);
        }
        else
        {
            # Match attempt failed
        }
}

$final_data = array();
$totalDifference = 0;

foreach ($data_aldi as $key => $value)
{
        //echo $key."<br>";
        // TODO: Rewrite as for loop. Once one element has been matched, don't check it again against the next item in the database
        foreach ($ocr as $key2 => $value2)
        {
                $wLev = weightedLevenshtein($value2["product"], $key);
                if ($wLev > 0.65)
                {
                        //echo $wLev."<br>";
                        $difference = $value2["price"] - $data_colruyt[$value["id"]];
                        $final_data[] = array(
                                "product" => ucwords($key),
                                "difference" => $difference,
                                "price" => $data_colruyt[$value["id"]]
                        );
                        $totalDifference += $difference;
                        break;
                }
                else
                {
                        //echo $wLev."<br>";
                }
        }
}

$final_data[] = array(
        "product" => "Totaal prijsverschil",
        "difference" => $totalDifference,
        "price" => 0
);

function weightedLevenshtein($str1, $str2)
{
        $levenshtein = levenshtein($str1, strtoupper($str2));
        //echo $levenshtein." ".$str1." - ".$str2."<br>";
        $weightedLevenshtein = 1 - ($levenshtein / strlen($str2));
        return $weightedLevenshtein;
}

//print_r($ocr);
//print_r($final_data);

echo json_encode($final_data);