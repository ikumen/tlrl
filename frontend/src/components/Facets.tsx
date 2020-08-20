import React, { useContext } from 'react';
import { useLocation, Link } from 'react-router-dom';
import * as Bookmark from '../components/bookmark';
import { parseQueryString, paramsToQueryString } from '../support';

function Facets() {
  const location = useLocation();
  const ctx = useContext(Bookmark.BookmarkContext);
  const params = parseQueryString(location.search);
  const facets = ctx.facets!;
  const copyParams = (params: Map<string, string>, excludeKeys: string[]) => {
    const newParams = new Map(params);
    excludeKeys.forEach(k => newParams.delete(k));
    return newParams;
  }

  const rqs = paramsToQueryString(copyParams(params, ['page', 'readStatus']));
  const sqs = paramsToQueryString(copyParams(params, ['page', 'sharedStatus']));
  const tqs = paramsToQueryString(copyParams(params, ['page', 'tags']));
  const tagsParam = params.get('tags');

  return <>
    <div className="pa2 bg-near-white">
    <h6 className="mv0 fw6 f6 black-70">Read Status</h6>
    <ul className="list f6 ma0 pa0">
    {Object.entries(facets['readStatuses']).map(([k,cnt]) => <li key={k} className="mv2">
      {cnt !== 0 
        ? k === params.get('readStatus') 
          ? <><span className="fw5">{k}</span> <span className="f7 gray">x {cnt} (<Link className="f6" to={rqs ? `?${rqs}` : ``}>clear</Link>)</span></>
          : <><Link to={`?readStatus=${k}${rqs ? `&${rqs}` : ``}`} className="link">{k}</Link> <span className="f7 gray">x {cnt}</span></> 
        : <span className="gray">{k}</span>
      }
    </li>)}
    </ul>
    <h6 className="mb2 mt3 fw6 f6 black-70">Shared Status</h6>
    <ul className="list f6 ma0 pa0">
    {Object.entries(facets.sharedStatuses).map(([k, cnt]) => <li key={k} className="mv2">
      {cnt !== 0
        ? k === params.get('sharedStatus')
          ? <><span className="fw5">{k}</span> <span className="f7 gray">x {cnt} (<Link className="f6" to={sqs ? `?${sqs}` : ``}>clear</Link>)</span></>
          : <><Link to={`?sharedStatus=${k}${sqs ? `&${sqs}` : ``}`} className="link">{k}</Link> <span className="f7 gray">x {cnt}</span></> 
        : <span className="gray">{k}</span>
      }
    </li>)}
    </ul>
    </div>
    <div className="pa1">
    <h5 className="mb2 mt3 fw6 f6 black-70">Related Tags</h5>
    <ul className="list f6 ma0 pa0">
    {params.get('tags') && (params.get('tags') || '').split(',').map(t => <li key={t} className="mb2 pb1">
      <span className="">{t}</span> <span className="f7 gray">(<Link className="f6" to={`?${tqs ? `${tqs}&tags=` : 'tags='}${tagsParam?.split(',').filter(p => p !== t).join(",")}`}>clear</Link>)</span>
    </li>)}
    {facets.tags.map(t => <li key={t.id} className="mb2 pb1">
      <span className="pv0 ph2 bg-light-yellow br1">
        <Link to={`?tags=${tagsParam ? `${tagsParam},${t.id}` : t.id}${tqs ? `&${tqs}` : ``}`} className="link">{t.id}</Link></span> <span className="f7 gray">x {t.count}</span>
    </li>)}
    </ul>
    </div>
  </>
}

export default Facets;