document.addEventListener("DOMContentLoaded", onLoad, false);

function onLoad() {
  chrome.tabs.query({ active: true, lastFocusedWindow: true }, (tabs) => {
    let url = tabs[0].url;

    var button = document.createElement("a");
    button.href = `https://autoshop-ic.vercel.app/api?url=${url}`;
    button.target = "_blank";
    button.innerText = "Get Shopping List";

    button.style.cssText =
      "display: block; margin: auto; padding: 10px; background-color: #4CAF50; color: white; text-decoration: none; font-size: 20px; text-align: center;";

    document.getElementById("button").appendChild(button);
  });
}
