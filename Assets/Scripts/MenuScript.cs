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

    public void Play()
    {
        Settings.treadmillMode = true;
        ChangeScene("Game");
    }

    public void PlayDemo()
    {
        Settings.treadmillMode = false;
        ChangeScene("Game");
    }
}
