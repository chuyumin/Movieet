import React from 'react';
import {Rate, Tag} from 'antd';
import styles from './MovieBanner.css';

import example from '../../assets/img/bg1.png';

function MovieBanner({movie}) {


    return (

        <div className={styles.banner}>
            <div className={styles.bg}>
                <div className={styles.bg_wrapper}>
                    <div className={styles.bg_img} style={{backgroundImage: `url(${movie.backgroundPoster})`}}/>
                    <div className={styles.overlay}/>
                </div>


            </div>

            <div className={styles.text}>
                <div className="container">
                    <p className={styles.date}>{movie.releaseDate}</p>
                    <h1>{movie.originTitle}</h1>
                    { movie.title !== movie.originTitle ?
                        <p className={styles.alias}>{movie.title}</p>
                        : null
                    }
                    { movie.titleCN !== movie.originTitle ?
                        <p className={styles.alias}>{movie.titleCN}</p>
                        : null
                    }
                    <div className={styles.genre_tags}>
                        {movie.genre ?
                            movie.genre.map((genre) =>
                                <Tag key={genre.id}>{genre.value}</Tag>
                            ) : null
                        }
                    </div>
                </div>
            </div>

        </div>
    );
}

export default MovieBanner;
