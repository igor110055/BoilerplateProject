import React from "react";
import { Controller } from "react-hook-form";
import {Select,MenuItem,FormControl,InputLabel,  FormControlLabel,
    FormLabel,
    Radio,
    RadioGroup,} from '@mui/material'
import { FormInputProps } from "./FormInputProps";
const options = [
  {
    label: "남자",
    value: "M",
  },
  {
    label: "여자",
    value: "F",
  },
  {
    label: "기타",
    value: "O",
  },
];
export const GenderRadio: React.FC<FormInputProps> = ({ name,control,label,rules }) => {
  const generateRadioOptions = () => {
    return options.map((singleOption,index) => (
      <FormControlLabel
        value={singleOption.value}
        label={singleOption.label}
        control={<Radio />}
        key={index}
      />
    ));
  };  

  return (
  
        <FormControl fullWidth variant="outlined">
            <FormLabel htmlFor={name}>{label}</FormLabel>                
        <Controller
            name={name}
            control={control}
            rules={ rules}
            render={({ field: { onChange, value} }) => (

                <>
                <RadioGroup 
                value={value}
                onChange={onChange}
                row={true}                  
                
                >
                {generateRadioOptions()}
                </RadioGroup>
                </>
            )}
            />
        </FormControl>
        )
};