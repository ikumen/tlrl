import React from 'react';
import { Link } from 'react-router-dom';

const nextLabel = "Next >>";
const prevLabel = "<< Previous";

/** For pagination that has results */
export interface ResultPage<T> extends PagingDetails {
  contents: T[];
}

/** For pagination, we require the following props/attributes */
export interface PagingDetails {
  page: number;
  size: number;
  total: number;
  first: boolean;
  last: boolean;
}


type NavProps = {
  label: string;
  page?: number;
}

function EnabledNav({label, page}: NavProps) {
  return <Link to={`?page=${page}`} className="link enabled always underline-hover f6 ml3">{label}</Link>;
}

function DisabledNav({label}: NavProps) {
  return <span className="black-30 f6 ml3">{label}</span>;
}

function Pagination({first, last, page}: PagingDetails) {
  return <div className="tr">
    {first ? <DisabledNav label={prevLabel} /> : <EnabledNav label={prevLabel} page={page - 1} />}
    {last ? <DisabledNav label={nextLabel} /> : <EnabledNav label={nextLabel} page={page + 1} />}
  </div>
}

export default Pagination;
