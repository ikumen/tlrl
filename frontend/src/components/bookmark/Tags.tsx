import React, { useEffect } from 'react';
import { Tag } from './types';

type TagsProps = {
  id: string,
  editable: boolean,
  tags?: Tag[],
  onChange: (attrName: string, value: any) => void,
}

/**
 * Converts a list of Tag objects to comma separated string of tags
 * @param {Tag[]} tags list of Tags 
 */
const tagsToString = (tags: Tag[]) => tags.map(t => t.id).join(', ');

/**
 * Converts string (comma separated) of tags into list of tags
 * @param {string} tagsString string representing list of tags to parse
 */
const stringToTags = (tagsString: string) => (
  tagsString.trim() /* clean up original string */
    .split(',') /* tags are separated by , */
    .map(t => t.trim() /* clean up each tag */
      .replace(/\s+/g, '-') /* tag words can be separated by dashes */
      .replace(/[^a-z0-9-]/gi, '')) /* remove all other special chars */
    .filter(t => t !== '') /* drop empty strings */
    .map(t => ({id: t}))
);

function Tags({ id, tags, editable, onChange }: TagsProps) {
  const inputRef = React.createRef<HTMLInputElement>();

  /**
   * Whenever we update the tags, notify our parent.
   */
  const parseTags = (evt: React.ChangeEvent<HTMLInputElement>) => {
    onChange(id, stringToTags(evt.currentTarget.value || ''));
  }

  /** 
   * Whenever we get an update from our parent, update our input form. Only
   * update when we are not editing the form ourselves, otherwise funny stuff
   * happens.
   */
  useEffect(() => {
    if (inputRef.current && !editable && tags != null) {
      inputRef.current.value = tagsToString(tags);
    }
  }, [inputRef, tags, editable]);

  return <div className="ma0 pa0">
    <input type="text"
      className="ma0 pv0 f6 w-100 bw0 bg-washed-yellow"
      placeholder="tags"
      hidden={!editable} 
      onChange={parseTags} 
      ref={inputRef} 
    />
    <div hidden={editable}>{tags && tags.map(tag =>
      <span key={tag.id} className="mr1 pv0 ph2 bg-light-yellow br1 f6">{tag.id}</span>
    )}</div>
  </div>
}

export default Tags;