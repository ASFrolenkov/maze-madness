import { css } from "styled-components";

class Logger {
  private pallette = {
    
  }

  info(tag: string, info?: string, ...output: unknown[]) {
    const labelStyle = css`
      color: rgb(173, 173, 173);
      margin-right: 4px;
    `;

    const tagStyle = css`
      background-color: rgb(154, 231, 255);
      color: rgb(2, 31, 70);
      padding: 2px;
      border-radius: 6px;
      margin-right: 8px;
      text-align: center;
    `;

    const outputStyled = css`
      color: rgb(154, 231, 255);
    `;

    console.log(
      `%cINFO%c ${tag} %c${info || ""}`,
      labelStyle[0],
      tagStyle[0],
      outputStyled[0],
      ...output
    );
  }

  trace(...output: unknown[]) {
    console.trace(...output);
  }

  error(...output: unknown[]) {
    console.error(...output);
  }

  warning(...output: unknown[]) {
    console.warn(...output);
  }

  debug(...output: unknown[]) {
    console.debug(...output);
  }

  table(...output: unknown[]) {
    console.table(...output);
  }
}

export const logger = new Logger();
