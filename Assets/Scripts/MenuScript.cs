using UnityEngine;
using UnityEngine.SceneManagement;

public class MenuScript : MonoBehaviour
{

    public GameObject menuPage;
    public GameObject controlsPage;
    public GameObject loadingText;

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
        ShowLoadingText();
        Settings.treadmillMode = true;
        ChangeScene("Game");
    }

    public void PlayDemo()
    {
        ShowLoadingText();
        Settings.treadmillMode = false;
        ChangeScene("Game");
    }

    public void ShowControls()
    {
        menuPage.SetActive(false);
        controlsPage.SetActive(true);
    }

    public void HideControls()
    {
        menuPage.SetActive(true);
        controlsPage.SetActive(false);
    }

    void ShowLoadingText()
    {
        menuPage.SetActive(false);
        loadingText.SetActive(true);
    }
}
