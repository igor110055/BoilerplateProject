import React  from "react";
import type { EditorProps } from 'react-data-grid';
export declare type Maybe<T> = T | undefined | null;

const ROW_STATUS = "_row_status"

function autoFocusAndSelect(input: HTMLInputElement | null) {
    input?.focus();
    input?.select();
  }
  

interface IGridPopupProps {
    p:any;
}


//https://github.com/adazzle/react-data-grid/issues/2240
const GridTextEditor =  <TRow, TSummaryRow = unknown>({
    row,
    column,
    onRowChange,
    onClose
  }: EditorProps<TRow, TSummaryRow>) => {
      //console.log({    row      })
      //console.log((row  as any)[ROW_STATUS])
      const tmp =(row  as any)[ROW_STATUS];
      let row_status="";

      if(tmp=="C"){
        row_status="C";
      } else {
        row_status="U";
      }
    return (
        <input
        className="rdg-text-editor"
        ref={autoFocusAndSelect}
        value={row[column.key as keyof TRow] as unknown as string}
        onChange={(event) => onRowChange({
          ...row,
          "_row_status": row_status,
          [column.key]: event.target.value
        })
        }
        onBlur={() => onClose(true)}
      />
    );
}

export default GridTextEditor;
