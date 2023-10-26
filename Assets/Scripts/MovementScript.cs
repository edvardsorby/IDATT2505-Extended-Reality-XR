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

    private float maxAngle = 25.0f;
    private float minAngle = 0;
    private float minSpeed = 0;

    private float angleStep = 0.1f;
    private float currentAngle = 0;

    public SkyboxCamera skyboxScript;


    // Start is called before the first frame update
    void Start()
    {
        controller = gameObject.AddComponent<CharacterController>();
        
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
            playerSpeed += 3.6f;
            TreadmillSingleton.Instance.Speed += 100;
            await TreadmillSingleton.Instance.Machine.Controller.SetTargetSpeed((ushort)TreadmillSingleton.Instance.Speed);

        }
        else if (Input.GetKeyDown(KeyCode.S))
        {
            playerSpeed -= 3.6f;
            TreadmillSingleton.Instance.Speed -= 100;
            await TreadmillSingleton.Instance.Machine.Controller.SetTargetSpeed((ushort)TreadmillSingleton.Instance.Speed);

            if (playerSpeed < minSpeed)
            {
                playerSpeed = minSpeed;
            }
        }

        Vector3 move = new Vector3(0, 0, 1);
        controller.Move(move * Time.deltaTime * playerSpeed);

        if (move != Vector3.zero)
        {
            gameObject.transform.forward = move;
        }


        if (Input.GetKeyDown(KeyCode.UpArrow))
        {
            Debug.Log("Up");
            currentAngle += angleStep;
            TreadmillSingleton.Instance.Incline += 1;
            UpdateIncline();
        }
        else if (Input.GetKeyDown(KeyCode.DownArrow))
        {
            Debug.Log("Down");
            currentAngle -= angleStep;

            TreadmillSingleton.Instance.Incline -= 1;
            UpdateIncline();
        }
        if (currentAngle < minAngle)
        {
            currentAngle = minAngle;
        }
        else if (currentAngle > maxAngle)
        {
            currentAngle = maxAngle;
        }


        Vector3 newRotation = new Vector3(currentAngle, 0, 0);
        camera.transform.eulerAngles = newRotation;

        skyboxScript.SkyBoxRotation = -newRotation;


        //Vector3 newRotation = new Vector3(currentAngle, 180, 0);
        //ground.transform.eulerAngles = newRotation;

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

        TreadmillSingleton.Instance.Speed = 0;
        await TreadmillSingleton.Instance.Machine.Controller.SetTargetSpeed((ushort)TreadmillSingleton.Instance.Speed);

        TreadmillSingleton.Instance.Incline = 0;
        await TreadmillSingleton.Instance.Machine.Controller.SetTargetInclination((ushort)TreadmillSingleton.Instance.Incline);
        Debug.Log("Resetting speed and angle");



        //StartCoroutine(WaitCoroutine());

        await TreadmillSingleton.Instance.Machine.Disconnect();
        Debug.Log("Treadmill disconnected");


        UnityEditor.EditorApplication.isPlaying = false;
        Application.Quit();
    }

    IEnumerator WaitCoroutine()
    {
        yield return new WaitForSeconds(5);
    }
}
