using System;
using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class LogicScript : MonoBehaviour
{

    public TMP_Text speedText;
    public TMP_Text angleText;
    public TMP_Text distanceText;
    
    // Start is called before the first frame update
    void Start()
    {
        speedText.text = "UI";
    }

    // Update is called once per frame
    void Update()
    {
        //speedText.text = "speed";
    }

    public void SetSpeedText(float speed, float step) {
        string speedString = "Speed: " + Math.Round(speed/step).ToString() + " km/h";
        speedText.text = speedString;
    }

    public void SetAngleText(float angle)
    {
        string angleString = "Angle: " + Math.Round(angle, 1).ToString() + "°";
        angleText.text = angleString;
    }

    public void SetDistanceText(float distance, float step)
    {
        string distanceString = "Distance: " + Math.Round(distance/step/3.6).ToString() + " m";
        distanceText.text = distanceString;
    }
}
