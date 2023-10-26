using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ChangeSkyboxScript : MonoBehaviour
{

    public Skybox currentSkybox;

    public Material[] skyboxes;

    private int i = 0;

    // Start is called before the first frame update
    void Start()
    {
        

        


    }

    // Update is called once per frame
    void Update()
    {
        if (Input.GetKeyDown(KeyCode.Space))
        {
            Debug.Log("Space");

            i++;
            if (i == skyboxes.Length) i = 0;

            currentSkybox.material = skyboxes[i];

            RenderSettings.skybox = skyboxes[i];
            DynamicGI.UpdateEnvironment();
            
        }
    }
}
