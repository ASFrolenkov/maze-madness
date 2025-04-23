import axios from "axios";

export const axiosMM = axios.create({
  baseURL: import.meta.env.VITE_HTTP_ADDRESS + "/api/v1",
  timeout: 1000,
  withCredentials: true,
});
