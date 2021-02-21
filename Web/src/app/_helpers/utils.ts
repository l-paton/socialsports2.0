import { Event } from "../models/Event";
import { User } from "../models/User";

export function calculateAge(birth: string): number {
  const today = new Date();
  const birthday = new Date(birth);

  let age = today.getFullYear() - birthday.getFullYear();
  const m = today.getMonth() - birthday.getMonth();
  if (m < 0 || (m === 0 && today.getDate() < birthday.getDate())) {
    age--;
  }
  return age;
}

export function meetTheRequirements(event: Event, age: number, gender: string, score: number): boolean {

  if (event.requirement.minAge > 0 && age < event.requirement.minAge) {
    return false;
  }
  if (event.requirement.maxAge > 0 && age > event.requirement.maxAge) {
    return false;
  }
  if (event.requirement.gender && event.requirement.gender.toUpperCase() != gender.toUpperCase()) {
    return false;
  }
  if (event.requirement.reputation && event.requirement.reputation > score) {
    return false;
  }

  return true;
}

export function isApplicant(applicants: User[], id: number): boolean {
  for (let j of applicants) {
    if (j.id === id) {
      return true;
    }
  }
  return false;
}

export function isParticipant(participants: User[], id: number): boolean {
  for (let i of participants) {
    if (i.id === id) {
      return true;
    }
  }
  return false;
}

export function eventIsFull(event: Event): boolean {
  if (event.maxParticipants > 0) {
    if (event.maxParticipants <= event.participants.length) {
      return true;
    }
  }
  return false;
}
