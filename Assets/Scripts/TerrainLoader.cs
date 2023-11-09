using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class TerrainLoader : MonoBehaviour
{
    private GameObject[] terrain;
    private GameObject[] player;

    // Start is called before the first frame update
    void Start()
    {
        terrain = GameObject.FindGameObjectsWithTag("Terrain");
        player = GameObject.FindGameObjectsWithTag("Player");
    }

    // Update is called once per frame
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