import { useState } from "react";
import { GameUI } from "Types/game";
import axios from "axios";
import { Wrapper } from "./styled";

export const MainMenuUI: GameUI = ({ game }) => {
  const [currentForm, setCurrentForm] = useState<"login" | "registration">(
    "login"
  );

  const submitForm = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    const data = new FormData(event.target as HTMLFormElement);
    // game.game?.events.emit("emitSocket", "playerCreate", name);

    if (currentForm === "login") {
      axios.post(import.meta.env.VITE_HTTP_ADDRESS + "/v1/login", {
        username: data.get("username"),
        password: data.get("password"),
      });
    }
    if (currentForm === "registration") {
      axios.post(import.meta.env.VITE_HTTP_ADDRESS + "/v1/register", {
        username: data.get("username"),
        password: data.get("password"),
        email: data.get("email"),
      });
    }
  };

  return (
    <Wrapper onSubmit={submitForm}>
      <input type="username" placeholder="username" name="username" required />
      <input type="password" name="password" placeholder="password" required />
      {currentForm === "registration" && (
        <input type="email" name="email" placeholder="email" required />
      )}
      <div>
        {currentForm === "login" && (
          <a
            onClick={(event) => {
              event.preventDefault();
              setCurrentForm("registration");
            }}
          >
            Registration
          </a>
        )}
        <button type="submit">submit</button>
      </div>
    </Wrapper>
  );
};
