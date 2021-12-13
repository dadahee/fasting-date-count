const XMLHttpRequest = require("xhr2");

let url = "http://203.252.166.78:9001/";

function timeout(ms) {
  return new Promise((resolve) => setTimeout(resolve, ms));
}

async function main() {
  var xhr = new XMLHttpRequest();
  for (let i = 1; i <= 50000; i++) {
    // GET test
    xhr.open("GET", url, true); // true for asynchronous
    xhr.send(null);
    console.log("GET " + i);

    await timeout(100);
  }
}

main();
