import { useState, useEffect } from 'react'
import './App.css'

const API_BASE_URL = 'http://localhost:8080/api/cards';

function App() {
  const [cards, setCards] = useState([]);
  const [filteredCards, setFilteredCards] = useState([]);
  const [searchQuery, setSearchQuery] = useState('');
  const [sortBy, setSortBy] = useState('');
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    name: '',
    type: '',
    rarity: '',
    image: null
  });

  // Fetch cards from backend
  const fetchCards = async () => {
    try {
      const url = sortBy ? `${API_BASE_URL}?sortBy=${sortBy}` : API_BASE_URL;
      const response = await fetch(url);
      const data = await response.json();
      setCards(data);
      setFilteredCards(data);
    } catch (error) {
      console.error('Error fetching cards:', error);
    }
  };

  // Initial load
  useEffect(() => {
    fetchCards();
  }, [sortBy]);

  // Search functionality
  useEffect(() => {
    if (searchQuery.trim() === '') {
      setFilteredCards(cards);
    } else {
      const filtered = cards.filter(card =>
        card.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
        card.type.toLowerCase().includes(searchQuery.toLowerCase())
      );
      setFilteredCards(filtered);
    }
  }, [searchQuery, cards]);

  // Handle form input changes
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  // Handle image upload
  const handleImageChange = (e) => {
    setFormData(prev => ({ ...prev, image: e.target.files[0] }));
  };

  // Add new card
  const handleSubmit = async (e) => {
    e.preventDefault();

    const formDataToSend = new FormData();
    formDataToSend.append('name', formData.name);
    formDataToSend.append('type', formData.type);
    formDataToSend.append('rarity', formData.rarity);
    if (formData.image) {
      formDataToSend.append('image', formData.image);
    }

    try {
      const response = await fetch(API_BASE_URL, {
        method: 'POST',
        body: formDataToSend
      });

      if (response.ok) {
        setShowModal(false);
        setFormData({ name: '', type: '', rarity: '', image: null });
        fetchCards();
      }
    } catch (error) {
      console.error('Error adding card:', error);
    }
  };

  // Delete card
  const handleDelete = async (id) => {
    if (!confirm('Are you sure you want to delete this card?')) return;

    try {
      const response = await fetch(`${API_BASE_URL}/${id}`, {
        method: 'DELETE'
      });

      if (response.ok) {
        fetchCards();
      }
    } catch (error) {
      console.error('Error deleting card:', error);
    }
  };

  // Convert byte array to base64 image
  const getImageSrc = (imageBytes) => {
    if (!imageBytes) return null;
    return `data:image/jpeg;base64,${imageBytes}`;
  };

  return (
    <div className="app-container">
      <header className="header">
        <h1>⚡ Pokemon Card Manager</h1>
        <p>Manage your Pokemon card collection with ease</p>
      </header>

      <div className="controls">
        <input
          type="text"
          className="search-bar"
          placeholder="Search by name or type..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />

        <select
          className="sort-select"
          value={sortBy}
          onChange={(e) => setSortBy(e.target.value)}
        >
          <option value="">Sort by...</option>
          <option value="name">Name</option>
          <option value="type">Type</option>
          <option value="rarity">Rarity</option>
          <option value="dateAdded">Date Added</option>
        </select>

        <button className="btn btn-primary" onClick={() => setShowModal(true)}>
          + Add Card
        </button>
      </div>

      {filteredCards.length === 0 ? (
        <div className="empty-state">
          <h3>No cards found</h3>
          <p>Start by adding your first Pokemon card!</p>
        </div>
      ) : (
        <div className="cards-grid">
          {filteredCards.map(card => (
            <div key={card.id} className="card">
              {card.image && (
                <img
                  src={getImageSrc(card.image)}
                  alt={card.name}
                  className="card-image"
                />
              )}
              <div className="card-content">
                <h3>{card.name}</h3>
                <div className="card-meta">
                  <span className="badge badge-type">{card.type}</span>
                  <span className="badge badge-rarity">{card.rarity}</span>
                  <span className="card-date">Added: {card.dateAdded}</span>
                </div>
                <button
                  className="btn btn-danger"
                  onClick={() => handleDelete(card.id)}
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {showModal && (
        <div className="modal-overlay" onClick={() => setShowModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h2>Add New Card</h2>
            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label>Card Name *</label>
                <input
                  type="text"
                  name="name"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="form-group">
                <label>Type *</label>
                <select
                  name="type"
                  value={formData.type}
                  onChange={handleInputChange}
                  required
                >
                  <option value="">Select type...</option>
                  <option value="Grass">Grass</option>
                  <option value="Fire">Fire</option>
                  <option value="Water">Water</option>
                  <option value="Electric">Electric</option>
                  <option value="Psychic">Psychic</option>
                  <option value="Fighting">Fighting</option>
                  <option value="Darkness">Darkness</option>
                  <option value="Metal">Metal</option>
                  <option value="Fairy">Fairy</option>
                  <option value="Dragon">Dragon</option>
                  <option value="Normal">Normal</option>
                </select>
              </div>

              <div className="form-group">
                <label>Rarity *</label>
                <select
                  name="rarity"
                  value={formData.rarity}
                  onChange={handleInputChange}
                  required
                >
                  <option value="">Select rarity...</option>
                  <option value="Common">Common</option>
                  <option value="Uncommon">Uncommon</option>
                  <option value="Rare">Rare</option>
                  <option value="Rare Holo">Rare Holo</option>
                  <option value="Ultra Rare">Ultra Rare</option>
                  <option value="Secret Rare">Secret Rare</option>
                  <option value="Promo">Promo</option>
                  <option value="Custom">Custom</option>
                </select>
              </div>

              <div className="form-group">
                <label>Card Image</label>
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleImageChange}
                />
              </div>

              <div className="form-actions">
                <button type="submit" className="btn btn-primary">
                  Add Card
                </button>
                <button
                  type="button"
                  className="btn btn-secondary"
                  onClick={() => setShowModal(false)}
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  )
}

export default App

