package converter

import java.util.Scanner


fun main() {
    val scanner = Scanner(System.`in`)
    var check = true

    loop@ while(check == true) {
        print("Enter what you want to convert (or exit): ")
        val a=scanner.nextLine()
        if (a != "exit") {
            request(a)
        }
        else break@loop
    }
}

fun request(requestline: String) {
    val reqArray =requestline.split(" ")
    if (reqArray.size < 4 || reqArray.size > 6) {
        println("Parse error")
        return
    }
    val amount = reqArray[0].toDoubleOrNull()
    var nameTo = reqArray[1]
    var nameFor = reqArray.last()
    if (amount == null) {
        println("Parse error")
        return
    }
    if (reqArray[1].toLowerCase() == "degree" || reqArray[1].toLowerCase() == "degrees" ) {
        nameTo = reqArray[1].toString() + " " + reqArray[2].toString()
    }
    if (reqArray[reqArray.size - 2] == "degree" || reqArray[reqArray.size - 2] == "degrees") {
        nameFor = reqArray[reqArray.size - 2].toString() + " " + reqArray.last().toString()
    }

    nameFor = nameFor.toLowerCase()
    nameTo = nameTo.toLowerCase()

    val chckDataTo = checkDataTo(nameTo)
    val chckDataFor = checkDataFor(nameFor)

    if (chckDataTo == chckDataFor && !chckDataFor) {
        println ("Conversion from ??? to ??? is impossible.")
    } else if (chckDataTo && !chckDataFor) {
        loop@ for (enum in Metraje.values()) {
            for (i in enum.names.indices) {
                if (nameTo == enum.names[i]) {
                    nameTo = enum.names[2]
                    break@loop
                }
            }
        }
        println("Conversion from $nameTo to ??? is impossible")
        return
    } else if (!chckDataTo && chckDataFor) {
        loop@ for (enum in Metraje.values()) {
            for (i in enum.names.indices) {
                if (nameFor == enum.names[i]) {
                    nameFor = enum.names[2]
                    break@loop
                }
            }
        }
        println("Conversion from ??? to $nameFor is impossible")
        return
    } else if (chckDataTo && chckDataFor) {
        val result = converter1(amount, nameFor, nameTo)
        println("${result[0]} ${result[1]} ${result[2]} ${result[3]} ${result[4]}")
    }

    //println("$amount $nameTo space $nameFor")
}


enum class Metraje(val size: Double, val names: Array<String>, val type: TypeOfMeasure) {
    METER(1.0, arrayOf("m", "meter", "meters"), TypeOfMeasure.LENGTH),
    KILOMETER(1000.0, arrayOf("km", "kilometer", "kilometers"), TypeOfMeasure.LENGTH),
    CENTIMETER(0.01, arrayOf("cm", "centimeter", "centimeters"), TypeOfMeasure.LENGTH),
    MILLIMETER(0.001, arrayOf("mm", "millimeter", "millimeters"), TypeOfMeasure.LENGTH),
    MILE(1609.35, arrayOf("mi", "mile", "miles"), TypeOfMeasure.LENGTH),
    YARD(0.9144, arrayOf("yd", "yard", "yards"), TypeOfMeasure.LENGTH),
    FOOT(0.3048, arrayOf("ft", "foot", "feet"), TypeOfMeasure.LENGTH),
    INCH(0.0254, arrayOf("in", "inch", "inches"), TypeOfMeasure.LENGTH),
    GRAM(1.0, arrayOf( "g", "gram", "grams"), TypeOfMeasure.WEIGHT),
    KILOGRAM(1000.0, arrayOf( "kg", "kilogram", "kilograms"), TypeOfMeasure.WEIGHT),
    MILLIGRAM(0.001, arrayOf( "mg", "milligram", "milligrams"), TypeOfMeasure.WEIGHT),
    POUND(453.592, arrayOf( "lb", "pound", "pounds"), TypeOfMeasure.WEIGHT),
    OUNCE(28.3495, arrayOf( "oz", "ounce", "ounces"), TypeOfMeasure.WEIGHT),
    CELSIUS(0.0, arrayOf("celsius", "degree Celsius", "degrees Celsius", "dc", "c", "degree celsius", "degrees celsius"), TypeOfMeasure.TEMPERATURE),
    FAHRENHEIT(0.0, arrayOf("f", "degree Fahrenheit", "degrees Fahrenheit", "fahrenheit", "df", "degree fahrenheit", "degrees fahrenheit"), TypeOfMeasure.TEMPERATURE),
    KELVIN(0.0, arrayOf("k", "Kelvin", "Kelvins", "kelvin", "kelvins"), TypeOfMeasure.TEMPERATURE),
    NULL(0.0, arrayOf("", "", ""), TypeOfMeasure.NULL);
}

enum class Peso (val size: Double, val names: Array<String>) {
    GRAM(1.0, arrayOf( "g", "gram", "grams")),
    KILOGRAM(1000.0, arrayOf( "kg", "kilogram", "kilograms")),
    MILLIGRAM(0.001, arrayOf( "mg", "milligram", "milligrams")),
    POUND(453.592, arrayOf( "lb", "pound", "pounds")),
    OUNCE(28.3495, arrayOf( "oz", "ounce", "ounces")),
    NULL(0.0, arrayOf("", "", ""));
}

enum class Temperatura (val names: Array<String>) {
    CELSIUS(arrayOf( "degree Celsius", "degrees Celsius", "celsius", "dc", "c")),
    FAHRENHEIT(arrayOf("degree Fahrenheit", "degrees Fahrenheit", "fahrenheit", "df", "f")),
    KELVIN(arrayOf( "Kelvin", "Kelvins", "k")),
    NULL(arrayOf("", "", ""));
}

enum class TypeOfMeasure {
    LENGTH,
    WEIGHT,
    TEMPERATURE,
    NULL
}

fun checkDataTo (nameTo: String): Boolean {
    return  Metraje.values().any() { it.names.contains(nameTo) }
}

fun checkDataFor (nameTo: String): Boolean {
    return  Metraje.values().any() { it.names.contains(nameTo) }
}

fun converter1(amount1: Double, nameFor:String, nameTo: String): Array<String> {
    //val input=line.split(" ")
    val amount= amount1
    val dimension=nameTo
    val newDemension = nameFor
    val listOfdata = Array<Metraje>(2) {Metraje.NULL}
    val listToPrint = Array<String>(5) {""}
    loop@ for (enum in Metraje.values()) {
        for (i in enum.names.indices) {
            if (dimension == enum.names[i]) {
                val NameToPrint = enum
                listOfdata[0] = NameToPrint
                if (dimension == newDemension) {
                    val NameToPrint2 = enum
                    listOfdata[1] = NameToPrint2
                }
            } else if (newDemension == enum.names[i]) {
                val NameToPrint2 = enum
                listOfdata[1] = NameToPrint2
            }
        }
    }
    if (listOfdata[0].type == listOfdata[1].type && listOfdata[1].type != TypeOfMeasure.TEMPERATURE) {
        if (amount >= 0) {
            listToPrint[0] = amount.toString()
        } else if (amount < 0 && listOfdata[0].type == TypeOfMeasure.WEIGHT ) {
            listToPrint[0] = "Weight shouldn't be negative"
            return listToPrint
        } else if (amount < 0 && listOfdata[0].type == TypeOfMeasure.LENGTH ) {
            listToPrint[0] = "Length shouldn't be negative"
            return listToPrint
        }
        if (listToPrint[0] == "1.0") listToPrint[1]=listOfdata[0].names[1]
        else listToPrint[1]=listOfdata[0].names[2]
        listToPrint[2]="is"
        listToPrint[3]=(amount * listOfdata[0].size / listOfdata[1].size).toString()
        if (listToPrint[3] == "1.0") listToPrint[4]=listOfdata[1].names[1]
        else listToPrint[4]=listOfdata[1].names[2]
        return listToPrint
    } else if (listOfdata[0].type == listOfdata[1].type && listOfdata[1].type == TypeOfMeasure.TEMPERATURE) {
        listToPrint[0]=amount.toString()
        if (listToPrint[0] == "1.0") listToPrint[1]=listOfdata[0].names[1]
        else listToPrint[1]=listOfdata[0].names[2]
        listToPrint[2]="is"
        listToPrint[3]= TemperaturaCalculation(amount, listOfdata[0], listOfdata[1]).toString()
        if (listToPrint[3] == "1.0") listToPrint[4]=listOfdata[1].names[1]
        else listToPrint[4]=listOfdata[1].names[2]
        return listToPrint
    } else {
        listToPrint[0] = "Conversion from "
        listToPrint[1] = listOfdata[0].names[2]
        listToPrint[2] = "to"
        listToPrint[3] = listOfdata[1].names[2]
        listToPrint[4] = " is impossible"
        return listToPrint
    }
}


fun TemperaturaCalculation (amount: Double, tempType1: Metraje , tempType2: Metraje): Double {
    when {
        tempType1 == Metraje.FAHRENHEIT && tempType2 == Metraje.CELSIUS -> return (amount - 32) * 5 / 9
        tempType1 == Metraje.FAHRENHEIT && tempType2 == Metraje.KELVIN -> return (amount + 459.67) * 5 / 9
        tempType1 == Metraje.FAHRENHEIT && tempType2 == Metraje.FAHRENHEIT -> return amount
        tempType1 == Metraje.KELVIN && tempType2 == Metraje.CELSIUS -> return amount - 273.15
        tempType1 == Metraje.KELVIN && tempType2 == Metraje.KELVIN -> return amount
        tempType1 == Metraje.KELVIN && tempType2 == Metraje.FAHRENHEIT -> return amount * 9 / 5 - 459.67
        tempType1 == Metraje.CELSIUS && tempType2 == Metraje.CELSIUS -> return amount
        tempType1 == Metraje.CELSIUS && tempType2 == Metraje.KELVIN -> return amount + 273.15
        tempType1 == Metraje.CELSIUS && tempType2 == Metraje.FAHRENHEIT -> return amount * 9 / 5 + 32
        else -> return 0.0
    }
}