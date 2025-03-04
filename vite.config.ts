import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import { resolve } from "path";

export default defineConfig({
  base: "./",
  plugins: [react()],
  server: {
    port: 8080,
    host: "127.0.0.1",
  },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          phaser: ["phaser"],
        },
      },
    },
    minify: "terser",
    terserOptions: {
      compress: {
        passes: 2,
      },
      mangle: true,
      format: {
        comments: false,
      },
    },
    emptyOutDir: true,
    assetsInlineLimit: 0,
  },
  preview: {
    port: 8081,
  },
  resolve: {
    alias: {
      Types: resolve(__dirname, "./src/types"),
      Assets: resolve(__dirname, "./src/assets"),
      Sockets: resolve(__dirname, "./src/socket/Sockets"),
      SocketEvent: resolve(__dirname, "./src/socket/events"),
      Entities: resolve(__dirname, "./src/entities"),
      Scenes: resolve(__dirname, "./src/scenes"),
      Utils: resolve(__dirname, "./src/utils"),
      UI: resolve(__dirname, "./src/UI"),
    },
  },
});
