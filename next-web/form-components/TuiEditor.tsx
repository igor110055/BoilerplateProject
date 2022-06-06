//https://myeongjae.kim/blog/2020/04/05/tui-editor-with-nextjs

import dynamic from 'next/dynamic';
import * as React from 'react';
import { Editor as EditorType, EditorProps } from '@toast-ui/react-editor';
import { TuiEditorWithForwardedProps } from '@/form-components/TuiEditorWrapper';
import '@toast-ui/editor/dist/toastui-editor.css';

interface EditorPropsWithHandlers extends EditorProps {
  onChange?(value: string): void;
}

const Editor = dynamic<TuiEditorWithForwardedProps>(() => import("@/form-components/TuiEditorWrapper"), { ssr: false });
const EditorWithForwardedRef = React.forwardRef<EditorType | undefined, EditorPropsWithHandlers>((props, ref) => (
  <Editor {...props} forwardedRef={ref as React.MutableRefObject<EditorType>} />
));


  interface Props extends EditorProps {
    onChange(value: string): void;
  
    valueType?: "markdown" | "wysiwyg";
  }
  
    const TuiEditor: React.FC<Props> = (props) => {
        const { initialValue, previewStyle, height, initialEditType, useCommandShortcut } = props;
        const editorRef = React.useRef<EditorType>();
        const handleChange = React.useCallback(() => {
            if (!editorRef.current) {
                return;
            }

            const instance = editorRef.current.getInstance();
            const valueType = props.valueType || "markdown";

            let tmp="";
            if(instance.isMarkdownMode()==true){
                tmp=instance.getMarkdown();
            } else {
                tmp=instance.getHTML();
            }
            props.onChange( tmp);
        }, [props, editorRef]);

        console.log({initialValue})
  
    return <div>
      <EditorWithForwardedRef
        {...props}
        initialValue={initialValue || ""}
        previewStyle={previewStyle || "vertical"}
        height={height || "600px"}
        initialEditType={initialEditType || "wysiwyg"}
        useCommandShortcut={useCommandShortcut || true}
        ref={editorRef}
        onChange={handleChange}
      />
    </div>;
  };
   
  export default TuiEditor;
