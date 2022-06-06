import { Controller } from "react-hook-form";
import {Select,MenuItem,FormControl,InputLabel, Theme, SxProps} from '@mui/material'
import send,{IParams} from '@/utils/send';
import React, { useState,useEffect ,forwardRef,useImperativeHandle} from "react";

export interface IFormSelectProps {
    name: string;
    control: any;
    label: string;
    setValue?: any;
    rules?:any;
    InputProps?:any;
    options?: IselectOption[];
    readOnly?:boolean;
    sx?: SxProps<Theme>;
  }

  export interface  IselectOption {
    value:string
    text:string
}  


export const FormSelect = (props: IFormSelectProps) => {
    const [selectOptions,setSelectOptions] = useState<IselectOption[]>([]);
    useEffect(()=>{
        if(props.options){            
            setSelectOptions(props.options);        
        }
    },[props.options]);
    

  return (
    <FormControl fullWidth sx={props.sx} variant="outlined" size="small">
    <InputLabel htmlFor={props.name}>{props.label}</InputLabel>
    <Controller
      name={props.name}
      control={props.control}
      rules={ props.rules}
      render={({ field: { onChange, value} ,
        fieldState: { error },
        formState,
    }) => {
          //console.log({value})
          return (
              <>
            <Select 
              value={value || ""}
              onChange={onChange}
              label={props.label}
              labelId={props.name}          
              >
                  {
    
                    selectOptions.map((option:IselectOption) => (
                  <MenuItem key={option.value} value={option.value}>
                        {option.text}
                    </MenuItem>
                    )
                )
                }
            </Select>
            </>
          )

      } } 
      defaultValue="" // make sure to set up defaultValue
    />
  </FormControl>
  );
};




