using UnityEngine;
using UnityEngine.SceneManagement;

public class MenuScript : MonoBehaviour
{

    public void ChangeScene(string sceneName)
    {
        SceneManager.LoadScene(sceneName);
    }

    public void Quit()
    {
        UnityEditor.EditorApplication.isPlaying = false;
        Application.Quit();
    }

    public void PlayVR()
    {
        Settings.treadmillMode = true;
        Settings.vrMode = true;
        ChangeScene("Game");
    }

    public void Play()
    {
        Settings.treadmillMode = true;
        Settings.vrMode = false;
        ChangeScene("Game");
    }

    public void PlayDemo()
    {
        Settings.treadmillMode = false;
        Settings.vrMode = false;
        ChangeScene("Game");
    }
}
