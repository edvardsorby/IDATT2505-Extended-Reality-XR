using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using FTMS;
using FTMS.Treadmill;

public class TreadmillSingleton : MonoBehaviour
{
    public static TreadmillSingleton Instance {get; private set;}

    public Treadmill Machine;
    public string TreadmillName = "WoodwayXXXX"; // The Bluetooth name of the fitness machine to connect to
    public int Speed = 0;
    public int Incline = 0;

    async void Start() {

        if (Settings.treadmillMode)
        {
            Debug.Log("Treadmill mode");

            Machine = new Treadmill();
            Debug.Log("Connecting to Fitness Machine...");
            await Machine.Connect(TreadmillName);

            Debug.Log("Fitness Machine connected");
        } else
        {
            Debug.Log("Demo mode");
        }

    }

    private void Awake() {
        if (Instance != null && Instance != this) {
            Destroy(this);
        }
        else {
            Instance = this;
        }
    }
}
