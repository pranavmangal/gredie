document.addEventListener("DOMContentLoaded", onLoad, false);

function onLoad() {
  chrome.tabs.query({ active: true, lastFocusedWindow: true }, (tabs) => {
    let url = tabs[0].url;

    var button = document.createElement("a");
    button.href = `http://localhost:3000/?url=${url}`;
    button.target = "_blank";
    button.innerText = "Get Shopping List";

    button.style.cssText =
      "display: block; margin: auto; padding: 20px; background-color: #080e2c; color: white; text-decoration: none; font-size: 20px; text-align: center; border-radius: 15px;";

    document.getElementById("button").appendChild(button);
  });
}
