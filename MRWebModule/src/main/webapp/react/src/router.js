import React from 'react';
import { Router, Route, Redirect } from 'dva/router';


import Movie from "./routes/Movie.js";


import Movies from "./routes/Movies.js";
import MovieDiscover from './routes/MovieDiscover';
import MovieCategory from "./routes/MovieCategory.js";



import MovieSearch from "./routes/MovieSearch.js";



function RouterConfig({ history }) {
  return (
    <Router history={history}>
      <Redirect from="/" to="/movies/discover" />
      <Redirect from="/movies" to="/movies/discover" />

      <Route path="/movies" component={Movies}>
        <Route path="/movies/discover" component={MovieDiscover} />
        <Route path="/movies/category" component={MovieCategory} />
        <Route path="/movies/search" component={MovieSearch} />
      </Route>
      <Route path="/movie/:id" component={Movie} />
    </Router>
  );
}

export default RouterConfig;
