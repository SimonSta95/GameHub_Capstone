import './LandingPage.css';

export default function LandingPage() {

    return (
        <>
            <section className="hero">
                <div className="hero-content">
                    <h1 className="hero-heading">Welcome to GameHub</h1>
                    <p className="hero-subheading">Your ultimate destination for discovering and enjoying your favorite games.</p>
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