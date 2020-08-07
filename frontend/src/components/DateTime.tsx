import React from "react";

function DateTime({date}: {date: Date}) {
  return <span>{new Date(date).toLocaleString()}</span>;
}

export default DateTime;
