using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TerrainLoader : MonoBehaviour
{
    private GameObject[] terrain;
    private GameObject[] player;

    //Finds the "Terain" objects and the "Player" object
    void Start()
    {
        terrain = GameObject.FindGameObjectsWithTag("Terrain");
        player = GameObject.FindGameObjectsWithTag("Player");
    }

    //If the player object has moved 200 units in front of the last terrain object, 
    //it will be teleported infront of the player to repeat the terrain endlessly.
    void Update()
    {
        foreach (GameObject a in terrain)
        {
            foreach (GameObject b in player)
            {
                if (a.transform.position.z + 1200 < b.transform.position.z)
                {
                    Vector3 newPosition = a.transform.position + Vector3.forward * 2000.0f;
                    a.transform.position = newPosition;
                }
            }
        }
    }
}