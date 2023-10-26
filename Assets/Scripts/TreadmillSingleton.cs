using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using FTMS;
using FTMS.Treadmill;

public class TreadmillSingleton : MonoBehaviour
{
    public static TreadmillSingleton Instance {get; private set;}

    public Treadmill Machine;
    public string TreadmillName = "WoodwayXXXX";
    public int Speed = 0;
    public int Incline = 0;

    async void Start() {
        Debug.Log("TEST");

        Machine = new Treadmill();
        await Machine.Connect(TreadmillName);

        Debug.Log("Fitness Machine connected");
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
