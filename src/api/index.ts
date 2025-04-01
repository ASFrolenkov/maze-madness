import axios from "axios";

export const axiosMM = axios.create({
  baseURL: import.meta.env.VITE_HTTP_ADDRESS,
  timeout: 1000,
});
