using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using static TreadmillSingleton;

public class MovementScript : MonoBehaviour
{

    //public GameObject ground;
    public Camera camera;
    private CharacterController controller;
    private Vector3 playerVelocity;
    private bool groundedPlayer;
    public float playerSpeed = 0.0f;
    private float gravityValue = -9.81f;

    public float speedStepGame = 1.8f;
    private int speedStepTreadmill = 100;


    private float maxAngle = 25.0f;
    private float minAngle = 0;
    private float minSpeed = 0;
    private float maxSpeedTreadmill = 20.0f;
    private float maxSpeed;


    private float angleStep = 1.0f;
    private float currentAngle = 0;

    public SkyboxCamera skyboxScript;

    public LogicScript logicScript;


    // Start is called before the first frame update
    void Start()
    {
        controller = gameObject.AddComponent<CharacterController>();
        maxSpeed = maxSpeedTreadmill * speedStepGame;
    }

    // Update is called once per frame
    async void Update()
    {
        groundedPlayer = controller.isGrounded;
        if (groundedPlayer && playerVelocity.y < 0)
        {
            playerVelocity.y = 0f;
        }

        if (Input.GetKeyDown(KeyCode.W))
        {
            IncreaseSpeed();
        }
        else if (Input.GetKeyDown(KeyCode.S))
        {
            DecreaseSpeed();
        }

        Vector3 move = new Vector3(0, 0, 1);
        controller.Move(move * Time.deltaTime * playerSpeed);

        if (move != Vector3.zero)
        {
            gameObject.transform.forward = move;
        }


        if (Input.GetKeyDown(KeyCode.UpArrow))
        {
            IncreaseAngle();
        }
        else if (Input.GetKeyDown(KeyCode.DownArrow))
        {
            DecreaseAngle();
        }


        Vector3 newRotation = new Vector3(currentAngle, 0, 0);
        camera.transform.eulerAngles = newRotation;

        skyboxScript.SkyBoxRotation = -newRotation;

        logicScript.SetSpeedText(playerSpeed, speedStepGame);
        logicScript.SetAngleText(currentAngle);
        logicScript.SetDistanceText(controller.transform.position.z, speedStepGame);

    

        playerVelocity.y += gravityValue * Time.deltaTime;
        controller.Move(playerVelocity * Time.deltaTime);

        if (Input.GetKeyDown(KeyCode.Escape))
        {
            Quit();
        }
    }


    async void UpdateIncline()
    {
        await TreadmillSingleton.Instance.Machine.Controller.SetTargetInclination((ushort)TreadmillSingleton.Instance.Incline);
    }


    async void Quit()
    {
        Debug.Log("Quitting game");

        if (Settings.treadmillMode)
        {


            TreadmillSingleton.Instance.Speed = 0;
            await TreadmillSingleton.Instance.Machine.Controller.SetTargetSpeed((ushort)TreadmillSingleton.Instance.Speed);

            TreadmillSingleton.Instance.Incline = 0;
            await TreadmillSingleton.Instance.Machine.Controller.SetTargetInclination((ushort)TreadmillSingleton.Instance.Incline);
            Debug.Log("Resetting speed and angle");



            //StartCoroutine(WaitCoroutine());

            await TreadmillSingleton.Instance.Machine.Disconnect();
            Debug.Log("Treadmill disconnected");

        }

        UnityEditor.EditorApplication.isPlaying = false;
        Application.Quit();
    }

    IEnumerator WaitCoroutine()
    {
        yield return new WaitForSeconds(5);
    }




    async void IncreaseSpeed()
    {
        playerSpeed += speedStepGame;
        if (playerSpeed > maxSpeed) playerSpeed = maxSpeed;


        if (Settings.treadmillMode)
        {
            TreadmillSingleton.Instance.Speed += speedStepTreadmill;
            await TreadmillSingleton.Instance.Machine.Controller.SetTargetSpeed((ushort)TreadmillSingleton.Instance.Speed);
        }

    }

    async void DecreaseSpeed()
    {
        playerSpeed -= speedStepGame;
        if (playerSpeed < minSpeed) playerSpeed = minSpeed;

        if (Settings.treadmillMode)
        {
            TreadmillSingleton.Instance.Speed -= speedStepTreadmill;
            await TreadmillSingleton.Instance.Machine.Controller.SetTargetSpeed((ushort)TreadmillSingleton.Instance.Speed);
        }
    }


    async void IncreaseAngle()
    {
        Debug.Log("Up");
        currentAngle += angleStep;


        if (currentAngle > maxAngle) currentAngle = maxAngle;

        if (Settings.treadmillMode)
        {
            TreadmillSingleton.Instance.Incline += Convert.ToInt32(Math.Round(angleStep))*10;
            UpdateIncline();
        }

    }

    async void DecreaseAngle()
    {
        Debug.Log("Down");
        currentAngle -= angleStep;

        if (currentAngle < minAngle) currentAngle = minAngle;

        if (Settings.treadmillMode)
        {
            TreadmillSingleton.Instance.Incline -= Convert.ToInt32(Math.Round(angleStep))*10;
            UpdateIncline();
        }
    }
        
}
