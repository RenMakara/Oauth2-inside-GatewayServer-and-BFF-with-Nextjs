/**
 * TypeScript types matching our backend DTOs
 */

export interface Product {
    id: number;
    name: string;
    price: number;
    imageUrl: string;
    categoryId: number;
}

export interface Category {
    id: number;
    name: string;
    description: string;
}

export interface DashboardData {
    products: Product[];
    categories: Category[];
}