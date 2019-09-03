import * as React from "react";

export const TestModule = () => {

  const postLocation = async () => {

    const payload = {

      x: 36.4511,
      y: 28.2278
    };
    await fetch("/trees/api/locations", {
      method: "post",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify(payload)
    })
  };

  return <div>

    <button onClick={postLocation}>POST location</button>
  </div>
};