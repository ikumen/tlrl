import React, { useEffect } from 'react';

type Props = {
  id: string;
  editable: boolean;
  shouldTruncate?: boolean;
  value: string;
  onChange: (id: string, value: string) => void;
}

function Editable({id, editable, value, onChange, shouldTruncate = false}: Props) {
  const editableRef = React.createRef<HTMLDivElement>();

  // Notify parent of changes
  const handleChange = (evt: React.FormEvent<HTMLDivElement>) => {
    onChange(id, evt.currentTarget.innerText.trim());
  }

  // Whenever we turn off editable, we want to restore the editables
  // changes to the original value. This works for cancel, but what if
  // we want to save the changed value, just make sure parent has saved
  // the changes before turning editable off.
  useEffect(() => {
    if (!editable && editableRef.current && editableRef.current.innerText !== value) {
      editableRef.current.innerText = value;
    }
  }, [editable, editableRef, value])

  return <div id={id} 
      className={`spacer ${editable ? 'editable bg-washed-yellow' : ''} ${shouldTruncate ? 'truncate' : ''} cf fl w-100 ma0`}
      contentEditable={editable}
      placeholder={`${id}`}
      ref={editableRef}
      onInput={handleChange}
      onBlur={handleChange}
      suppressContentEditableWarning={true}>
        {value}
    </div>;
}

export default Editable;

