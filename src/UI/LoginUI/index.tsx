import { useEffect, useRef, useState, useCallback } from "react";
import { GameUI } from "Types/game";
import { axiosMM } from "API";
import {
  LoginForm,
  RegistrationLink,
  RememberCheckbox,
  RememberLabel,
  RememberWrapper,
  SubmitWrapper,
} from "./styled";

const sendRegistartion = <ServerResponse,>(data: FormData) =>
  axiosMM.post<ServerResponse>("/auth/register", {
    username: data.get("username"),
    password: data.get("password"),
    confirmPassword: data.get("confirmPassword"),
    email: data.get("email"),
  });

export const LoginUI: GameUI = ({ game }) => {
  const [currentForm, setCurrentForm] = useState<"login" | "registration">(
    "login"
  );
  const [passwordError, setPasswordError] = useState(false);
  const [registerMessage, setRegisterMessage] = useState("");
  const checkboxRef = useRef<HTMLInputElement>(null);

  const sendLogin = useCallback(
    (username?: string, password?: string) =>
      axiosMM
        .post("/auth/login", {
          username,
          password,
        })
        .then(() => {
          game.scene?.registry.set("username", username);
          game.scene?.nextScene();
        }),
    [game.scene]
  );

  useEffect(() => {
    axiosMM.post<string>("/auth/validate").then((response) => {
      game.scene?.registry.set("username", response.data);
      game.scene?.nextScene();
    });
  }, []);

  const submitForm = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setPasswordError(false);
    const data = new FormData(event.target as HTMLFormElement);

    if (
      currentForm === "registration" &&
      data.get("confirmPassword") !== data.get("password")
    ) {
      setPasswordError(true);
      return;
    }

    if (currentForm === "login") {
      const username = String(data.get("username"));
      const password = String(data.get("password"));

      if (username && password) sendLogin(username, password);
    }

    if (currentForm === "registration") {
      sendRegistartion(data).then(() => {
        setCurrentForm("login");
        setRegisterMessage("Register success");
      });
    }
  };

  return (
    <LoginForm onSubmit={submitForm}>
      {registerMessage && <div>{registerMessage}</div>}
      <input type="username" placeholder="username" name="username" required />
      <input type="password" name="password" placeholder="password" required />
      {currentForm === "registration" && (
        <>
          <input
            type="password"
            name="confirmPassword"
            placeholder="confirm password"
            required
          />
          {passwordError && <div>Password does not match</div>}
          <input type="email" name="email" placeholder="email" required />
        </>
      )}

      <SubmitWrapper>
        <button type="submit">submit</button>
        {currentForm === "registration" && (
          <button onClick={() => setCurrentForm("login")}>back</button>
        )}
        {currentForm === "login" && (
          <RememberWrapper>
            <RememberLabel htmlFor="remember">remember me</RememberLabel>
            <RememberCheckbox type="checkbox" id="remember" ref={checkboxRef} />
          </RememberWrapper>
        )}
      </SubmitWrapper>

      {currentForm === "login" && (
        <>
          <RegistrationLink
            onClick={(event) => {
              event.preventDefault();
              setCurrentForm("registration");
            }}
          >
            Registration
          </RegistrationLink>
        </>
      )}
    </LoginForm>
  );
};
