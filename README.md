# Fluento

AI-powered, voice-first language learning app designed to help users **speak** a new language with confidence.

Fluento focuses on real conversation practice instead of only typing exercises. Users learn through AI voice tutoring, speaking missions, pronunciation feedback, roleplay scenarios, and personalized lesson flows inspired by modern voice-based language apps. [web:12][web:30]

## Features

- Voice-first language learning experience
- AI tutor for speaking practice
- Real-time speech-to-text transcription
- Pronunciation and fluency feedback
- Repeat-after-me and shadowing practice
- Roleplay conversations like travel, shopping, interviews, and daily life
- Personalized speaking roadmap
- Progress tracking with streaks, minutes spoken, and improvement insights
- Premium-ready architecture for subscriptions and scalable AI features

## Why this project

Most language learning apps make users read and type too much. Fluento is built around a better speaking loop:

1. The AI tutor speaks.
2. The learner listens.
3. The learner replies by voice.
4. The app transcribes and evaluates the response.
5. The learner gets feedback on pronunciation, grammar, and fluency.
6. The learner retries and improves.

This makes the product more useful for users who want real-world speaking confidence, which aligns with how voice-focused AI tutor apps present their value. [web:12][web:30]

## Core idea

Fluento is built as a private AI speaking tutor in your pocket.

The app is designed around:
- Listening comprehension
- Spoken replies
- Instant correction
- Repeat practice
- Real conversation simulation
- Daily speaking habits

## Screens

- Splash screen
- Welcome screen
- Onboarding flow
- Speaking goal setup
- Tutor personality selection
- Personalized speaking plan
- Demo voice lesson
- Paywall
- Home dashboard
- Live voice tutor screen
- Pronunciation feedback screen
- Roleplay conversation screen
- Progress analytics screen
- Mistakes review screen
- Settings and subscription screen

## Tech stack

You can adapt this section to your actual stack.

### Frontend
- Flutter or React Native
- Modular reusable UI components
- Mobile-first responsive design

### Backend
- Supabase or Firebase
- Authentication
- Analytics
- User progress storage

### AI and voice
- OpenAI-compatible LLM API
- Whisper or Deepgram for speech-to-text
- ElevenLabs or Azure TTS for text-to-speech

### Payments
- Stripe or Razorpay

## How it works

### Voice lesson flow
1. The app introduces a phrase or question using voice.
2. The user responds through the microphone.
3. Speech is converted into text.
4. The AI checks grammar, pronunciation, fluency, and intent.
5. Feedback is shown and spoken back to the learner.
6. The learner repeats until they improve.

### Practice modes
- AI voice chat
- Repeat-after-me mode
- Shadowing mode
- Listening response drills
- Conversation roleplay
- Mistake review

## Installation

Update the commands below based on your tech stack.

### Option 1: React Native / Expo
```bash
git clone https://github.com/your-username/fluento.git
cd fluento
npm install
npm run dev
