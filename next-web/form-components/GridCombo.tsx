import React from "react";
import {css} from '@emotion/css'
import { IselectOption } from "./FormSelect";
export declare type Maybe<T> = T | undefined | null;

const toolbarClassname = css`
  text-align: end;
  margin-block-end: 8px;
`;

//https://adazzle.github.io/react-data-grid/#/common-features
//https://github.com/adazzle/react-data-grid
//https://github.com/adazzle/react-data-grid/blob/main/website/demos/CustomizableComponents.tsx
//https://stackoverflow.com/questions/62210286/declare-type-with-react-useimperativehandle
const ROW_STATUS = "_row_status"


const textEditor = css`
  appearance: none;
  box-sizing: border-box;
  inline-size: 100%;
  block-size: 100%;
  padding-block: 0;
  padding-inline: 6px;
  border: 2px solid #ccc;
  vertical-align: top;
  color: var(--rdg-color);
  background-color: var(--rdg-background-color);
  font-family: inherit;
  font-size: var(--rdg-font-size);
  &:focus {
    border-color: var(--rdg-selection-color);
    outline: none;
  }
  &::placeholder {
    color: #999;
    opacity: 1;
  }
`;

export const textEditorClassname = `rdg-text-editor ${textEditor}`;

interface IGridComboProps {
    p:any;
    options?: IselectOption[];
}



const GridCombo = (props:IGridComboProps) =>{
    /*
    console.log(props.p);
    console.log(props.p.row);
    console.log(props.p.column.key);
    */

    const onChangeHanlder = (e: React.ChangeEvent<HTMLSelectElement>) => {
        e.preventDefault();
/*
        console.log(props.p.column.key);
        console.log(e.target.value );
        console.log(props.p.row );
        console.log("p");
        console.log(props.p);
*/        

        const tmp =(props.p.row  as any)[ROW_STATUS];
        let row_status="";
        if(tmp=="C"){
            row_status="C";
        } else {
            row_status="U";
        }
        props.p.onRowChange({ ...props.p.row, [props.p.column.key]: e.target.value , "_row_status":row_status }, false);
    }
    
    return (
        <select
            autoFocus
            className={textEditorClassname}
            value={props.p.row[props.p.column.key]}
            onChange={onChangeHanlder}
        >
            {
                props.options && 
                props.options.map((option:IselectOption,idx:number) => (
                    <option key={idx} value={option.value}>{option.text}</option>   
                    )
                )
            }
        </select>
    )
}

export default GridCombo;