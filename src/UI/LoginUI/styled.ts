import styled from "styled-components";

export const LoginForm = styled.form`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  padding: 10px;
  background-color: aliceblue;
  border: 1px solid red;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
  gap: 8px;
`;

export const SubmitWrapper = styled.div`
  display: flex;
  gap: 8px;
`;

export const RememberWrapper = styled.div`
  display: flex;
  gap: 5px;
  margin: 0 auto;
`;

export const RememberLabel = styled.label`
  align-self: center;
`;

export const RememberCheckbox = styled.input`
  cursor: pointer;
`;

export const RegistrationLink = styled.a`
  width: fit-content;
`;
