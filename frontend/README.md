# ⚡ Pokemon Card Manager - Frontend

A modern, responsive web application for managing your Pokemon card collection. Built with React and Vite, this frontend provides an intuitive interface to catalog, search, sort, and organize your Pokemon cards with image support.

## 🎯 Features

- **Card Management**: Add, view, and delete Pokemon cards from your collection
- **Image Upload**: Upload and store card images with each entry
- **Real-time Search**: Search cards by name or type with instant filtering
- **Flexible Sorting**: Sort your collection by:
  - Name
  - Type
  - Rarity
  - Date Added
- **Responsive Design**: Modern, mobile-friendly interface with smooth animations
- **Type Support**: All Pokemon card types (Grass, Fire, Water, Electric, Psychic, Fighting, Darkness, Metal, Fairy, Dragon, Normal)
- **Rarity Tracking**: Track card rarity levels (Common, Uncommon, Rare, Rare Holo, Ultra Rare, Secret Rare, Promo, Custom)

## 🛠️ Tech Stack

- **React 19.2** - UI framework
- **Vite 7.2** - Build tool and dev server
- **Tailwind CSS 3.4** - Utility-first CSS framework
- **ESLint** - Code linting and quality

## 📋 Prerequisites

- Node.js (v16 or higher recommended)
- npm or yarn
- Backend API running on `http://localhost:8081` (see `../backend` directory)

## 🚀 Getting Started

### Installation

```bash
# Install dependencies
npm install
```

### Development

```bash
# Start the development server
npm run dev
```

The application will be available at `http://localhost:5173` (or another port if 5173 is in use).

### Build

```bash
# Create production build
npm run build
```

### Preview Production Build

```bash
# Preview the production build locally
npm run preview
```

### Linting

```bash
# Run ESLint
npm run lint
```

## 🔌 API Configuration

The frontend connects to the backend API at `http://localhost:8081/api/cards`. If your backend runs on a different port or host, update the `API_BASE_URL` constant in `src/App.jsx`:

```javascript
const API_BASE_URL = 'http://localhost:8081/api/cards';
```

## 📁 Project Structure

```
frontend/
├── src/
│   ├── App.jsx          # Main application component
│   ├── App.css          # Component-specific styles
│   ├── index.css        # Global styles and Tailwind directives
│   ├── main.jsx         # Application entry point
│   └── assets/          # Static assets
├── public/              # Public static files
├── index.html           # HTML template
├── vite.config.js       # Vite configuration
├── tailwind.config.js   # Tailwind CSS configuration
└── package.json         # Dependencies and scripts
```

## 🎨 Key Components

### Main Features

- **Search Bar**: Real-time filtering by card name or type
- **Sort Dropdown**: Dynamic sorting options
- **Add Card Modal**: Form for adding new cards with image upload
- **Card Grid**: Responsive grid layout displaying all cards
- **Card Display**: Shows card image, name, type, rarity, and date added

### State Management

The application uses React hooks for state management:

- `useState` for local component state
- `useEffect` for data fetching and search filtering

## 🖼️ Image Handling

Card images are:

- Uploaded as files through the form
- Sent to the backend as multipart/form-data
- Stored in the database as byte arrays
- Displayed using base64 encoding

## 🌐 Browser Support

Modern browsers with ES6+ support:

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## 📝 License

This project is part of a personal Pokemon card collection manager.
