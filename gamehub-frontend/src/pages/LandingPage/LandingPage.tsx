import './LandingPage.css';
import githubLogo from "../../assets/github-mark.svg"
import {Button} from "@mui/material";
import {User} from "../../types.ts";

type LandingPageProps = {
    user?: User | null;
    onLogin: () => void;
};

export default function LandingPage(props: Readonly<LandingPageProps>) {

    return (
        <>
            <section className="hero">
                <div className="hero-content">
                    <h1 className="hero-heading">Welcome to GameHub</h1>
                    <p className="hero-subheading">Your ultimate destination for discovering and enjoying your favorite games.</p>
                    {!props.user && (
                        <Button
                            variant="contained"
                            color="secondary"
                            onClick={props.onLogin}
                            className="github-login-button"
                        >
                            <img src={githubLogo} alt="GitHub Logo" className="github-logo" /> Sign in with GitHub
                        </Button>
                    )}
                </div>
            </section>

            <section className="features">
                <h2 className="features-heading">Why Choose Us?</h2>
                <div className="feature-list">
                    <div className="feature">
                        <h3>Discover New Games</h3>
                        <p>Explore a vast library of games from various genres.</p>
                    </div>
                    <div className="feature">
                        <h3>Curated Collections</h3>
                        <p>Find games tailored to your interests and preferences.</p>
                    </div>
                    <div className="feature">
                        <h3>Community Reviews</h3>
                        <p>Read reviews and ratings from fellow gamers.</p>
                    </div>
                </div>
            </section>
        </>
    );
}