// Arduino code to send DS18B20 probe values over serial port

#include <OneWire.h>
#include <DallasTemperature.h>

const int ds_pin = 8;

OneWire ds(ds_pin);
DallasTemperature sensors(&ds);

void setup()
{
    pinMode(7, OUTPUT);
    digitalWrite(7, true); // using pin 7 as power for the probes

    Serial.begin(115200);
    delay(50);
    sensors.begin();
}

void loop()
{
    const int j = sensors.getDS18Count();

    sensors.requestTemperatures();
    delay(100);

    for (int i = 0; i < j; i++)
    {
        uint8_t address[8];

        if (!sensors.getAddress(address, i))
        {
            continue;
        }

        float temperature = sensors.getTempC(address);

        if (temperature < -100)
        {
            continue;
        }

        Serial.print("begin,");

        Serial.print(ds_pin);
        Serial.print(':');

        Serial.print(i);
        Serial.print(':');

        for (int x = 0; x < 8; x++)
        {
            Serial.print(address[x], 16);
        }

        Serial.print(',');
        Serial.print(temperature);
        
        Serial.println(",end");
    }
}
