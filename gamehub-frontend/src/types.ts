export type User = {
    id: string,
    username: string,
    gitHubId: string,
    avatarUrl: string,
    role: string,
    gameLibrary: GameAPI[],
}

export type Game = {
    id: string,
    title: string,
    genre: string,
    releaseDate: string,
    platforms: string[],
    developer: string,
    publisher: string,
    description: string,
    coverImage: string
}

export type GameAPIResponse = {
    count: number
    next: string,
    pervious: string,
    games: GameAPI[],
}

export type GameAPI = {
    id: string,
    title: string,
    genre: string[],
    releaseDate: string,
    platforms: string[],
    coverImage: string
}

export type UserGameLibraryAction = {
    userId: string,
    game: GameAPI
}

export type GameDetailAPIResponse = {
    id: number;
    name: string;
    description: string;
    released: string;
    background_image: string;
    platforms: platform[];
    developers: Developer[];
    genres: Genre[];
    publishers: Publisher[];
}

export type platform = {
    platform: {
        name: string
    }
}

export type Developer = {
    name: string;
};

export type Genre = {
    name: string;
};

export type Publisher = {
    name: string;
};

export type Note = {
    id: string;
    name: string;
    userId: string;
    gameId: string;
    title: string;
    content: string;
    category: string;
    created: string;
};

export type editNote = {
    name: string;
    title: string;
    content: string;
    category: string;
    created: string;
}

export type Review = {
    id: string;
    name: string;
    userId: string;
    gameId: string;
    username: string;
    rating: number;
    content: string;
    date: string;
};